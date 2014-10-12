class window.FeedModel extends Backbone.Model
  url: -> application.url 'feeds', { id: @get 'id' }

  fetchPosts: (callback) ->
    $.ajax {
      method: 'GET'
      url:    @url()
      success: (data) ->
        collection = new PostCollection()
        collection.add data.posts
        console.log data, collection
        callback collection
    }
