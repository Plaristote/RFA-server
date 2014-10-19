class window.FeedCollection extends Backbone.Collection
  model: FeedModel
  url: (params) -> application.url 'feeds', params

  constructor: ->
    application.current_user.on 'authenticate:connect', => @fetch()
    @fetch() if application.current_user.is_connected()?
    super

  fetch: ->
    $.ajax {
      method: 'GET',
      url:     @url(),
      success: (data) =>
        console.log data
        @reset()
        @add data.feeds
    }

  create_from_url: (url) ->
    $.ajax {
      method: 'POST'
      url:    @url { "feed[url]": url }
      success: =>
        application.notification "Succesfully added feed #{url}"
        @fetch()
      failure: ->
        application.notification "Could not add feed #{url}", 'error'
    }

