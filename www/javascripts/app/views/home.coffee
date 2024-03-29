class window.HomeView extends Backbone.View
  template:       JST['home']
  template_feeds: JST['menu_feeds']

  events: {
    "click .add-feed button":     "add_feed"
    "click #feed-list [data-id]": "show_feed"
  }

  constructor: () ->
    super
    @listenTo application.feeds,        'change reset add remove', @refresh_feeds
    @listenTo application.current_user, 'authenticate',            @refresh_user

  render: () ->
    @$el.html @template()
    @$user  = @$el.find('#user')
    @$feeds = @$el.find('#feed-list')
    @refresh_user()
    @refresh_feeds()
    $('body').empty().append(@$el)

  refresh_feeds: () ->
    @$feeds.html @template_feeds { collection: application.feeds }

  refresh_user: () ->
    console.log "refreshing user #{application.current_user.get 'email'} -> connected:#{application.current_user.is_connected()}"
    if application.current_user.is_connected()
      view = new SessionViews.Show()
      @$feeds.css 'visibility', 'visible'
    else
      view = new SessionViews.Create()
      @$feeds.css 'visibility', 'hidden'
    view.render()
    @$user.empty().append view.$el

  add_feed: (e) ->
    e.preventDefault()
    application.feeds.create_from_url @$el.find('.add-feed-url').val()

  show_feed: (e) ->
    e.preventDefault()
    feed_id = $(e.currentTarget).data 'id'
    Backbone.history.loadUrl "feeds/#{feed_id}"
