class window.FeedsController extends Backbone.Router
  routes: {
    "feeds":             "index"
    "feeds/:id":         "show"
    "feeds/create":      "create",
    "feeds/:id/destroy": "destroy"
  }

  index: ->
    ;

  show: (id) ->
    feed         = application.feeds.get id
    feed.fetchPosts (posts) =>
      console.log posts
      view       = new FeedView()
      view.feed  = feed
      view.posts = posts
      view.render()

  create: ->
    ;

  destroy: ->
    ;
