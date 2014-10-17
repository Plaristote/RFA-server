class window.FeedModel extends Backbone.Model
  url: -> application.url 'feeds', { id: @get 'id' }

  favicon: ->
    url = @get 'favicon'
    if url? and url != ''
      url
    else
      application.url 'assets/images/default-icon-rss.png'

  fetchPosts: (callback) ->
    $.ajax {
      method: 'GET'
      url:    @url()
      success: (data) =>
        collection = new PostCollection()
        collection.add data.posts
        model.feed = @ for model in collection.models
        callback collection
    }
