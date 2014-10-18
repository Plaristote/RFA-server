class window.FeedModel extends Backbone.Model
  url: -> application.url 'feeds', { id: @get 'id' }

  favicon: ->
    url = @get 'favicon'
    if url? and url != ''
      url
    else
      application.url 'assets/images/default-icon-rss.png'

  fetchPosts: (callback, data = {}) ->
    $.ajax {
      method:   'GET'
      url:      @url()
      data:     data
      success: (data) =>
        collection = new PostCollection()
        collection.add data.posts
        model.feed = @ for model in collection.models
        callback collection
    }

  fetchPage: (callback, page, items_per_page, highest_id) ->
    @fetchPosts callback, page: page, limit: items_per_page, highest_id: highest_id
