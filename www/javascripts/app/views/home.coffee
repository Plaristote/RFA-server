class window.HomeView extends Backbone.View
  template:       JST['home']
  template_feeds: JST['menu_feeds']

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
    console.log "refreshing feeds"
    @$feeds.html @template_feeds { collection: application.feeds }

  refresh_user: () ->
    console.log "refreshing user #{application.current_user.get 'email'} -> connected:#{application.current_user.is_connected()}"
    view = if application.current_user.is_connected()
      new SessionViews.Show()
    else
      new SessionViews.Create()
    view.render()
    @$user.empty().append view.$el
