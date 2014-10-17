class window.FeedView extends Backbone.View
  className: 'feed-page'
  template: JST['feed']

  events:
    "click .controls button.minimize": "minimize_posts" 
    "click .post     button.minimize": "minimize_post"
    "click .post     > a":             "open_post"
    "dblclick .post  > a":             "double_clicked_post"
    "click .post":                     "clicked_on_post"

  render: () ->
    @$el.html @template { feed: @feed, posts: @posts }
    $('#main-content').empty().append @$el
    for post in @posts.models
      post.on 'set_as_read', @set_post_as_read, @

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
    console.log $(e.currentTarget).parents('.post')
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
