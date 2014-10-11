class window.FeedCollection extends Backbone.Collection
  model: FeedModel
  url:   -> application.url 'feeds'

  constructor: ->
    application.current_user.on 'authenticate:connect', => @fetch()
    @fetch() if application.current_user.is_connected()?
    super

  fetch: ->
    $.ajax {
      method: 'GET',
      url:     @url(),
      success: (data) =>
        window.test_feeds = [ { id: 1, title: '9-Gag', favicon: 'http://assets-9gag-lol.9cache.com/static/00028/core/20140127_1390790346/img/favicon_v2.png', unread: 21 }, { id: 2, title: 'The Art of Manliness', favicon: 'http://aom.screenfour.com/wp-content/uploads/builder-favicon/6qBqUBP7i.ico', unread: 42 } ]
        console.log test_feeds
        @reset()
        @add test_feeds
    }
