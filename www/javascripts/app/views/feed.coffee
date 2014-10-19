class window.FeedView extends Backbone.View
  className:               'feed-page'
  template:                JST['feed']
  posts_template:          JST['posts']
  page:                    0
  items_per_page:          50
  infinite_scroll_enabled: false
  events:
    "click         .controls button.delete":         "delete_feed"
    "click         .controls button.minimize-all":   "minimize_posts" 
    "click .post > .controls button.minimize":       "minimize_post"
    "click .mark-as-read":   "on_mark_as_read"
    "click .mark-as-unread": "on_mark_as_read"
    "click .post > a":                               "open_post"
    "dblclick .post > a":                            "double_clicked_post"
    "click .post":                                   "clicked_on_post"

  render: () ->
    @$el.html @template { feed: @feed, posts: @posts }
    @append_posts @posts
    $('#main-content').empty().append @$el
    $(window).bind 'scroll', => @on_window_scrolled()

  delete_feed: () ->
    @feed.delete()
    Backbone.history.navigate 'home', true

  on_mark_as_read: (e) ->
    $post = @get_post_from_event e
    post  = @posts.get($post.data 'id')
    read  = $(e.currentTarget).hasClass 'mark-as-read'
    console.log "set post #{post.get 'id'} as read:", read
    post.setAsRead read

  on_window_scrolled: ->
    return if @infinite_scroll_enabled == false
    if $(window).scrollTop() == $(document).height() - window.innerHeight
      @infinite_scroll_enabled = false
      $('.loader', @$el).show()
      callback = (@append_posts.bind @)
      @feed.fetchPage callback, @page, @items_per_page, (@posts.models[0].get 'id')

  append_posts: (posts) ->
    $('.loader',     @$el).hide()
    $('.feed-posts', @$el).append @posts_template { posts: posts }
    for post in posts.models
      post.on 'set_as_read', @set_post_as_read, @
    @page += 1
    @infinite_scroll_enabled = true
    if posts.models != @posts.models
      @posts.add model for model in posts.models
      window.posts = @posts
    @delegateEvents()

  minimize_posts: (e) ->
    e.preventDefault()
    $('.post > .description', @$el).slideUp()

  minimize_post: (e) ->
    $post = @get_post_from_event e
    $post.find('.description').slideUp()

  open_post: (e) ->
    $post = @get_post_from_event e
    unless $post.find('.description').is(':visible')
      e.preventDefault()
      $post.find('.description').slideDown()

  double_clicked_post: (e) ->
    $post = @get_post_from_event e
    $post.find('.description').hide()

  get_post_from_event: (e) ->
    $(e.currentTarget).parents('.post')

  set_post_as_read: (post) ->
    $post = $(".post[data-id=#{post.get 'id'}]", @$el)
    if (post.get 'has_been_read') == true
      $post.addClass    'read'
    else
      $post.removeClass 'read'

  clicked_on_post: (e) ->
    unless $(e.target).is('button') or $(e.target).is('h2')
      post_id = $(e.currentTarget).data('id')
      post    = @posts.get post_id
      post.setAsRead()
