class window.PostModel extends Backbone.Model
  url: -> application.url "feeds/#{@feed.get 'id'}"

  setAsRead: (has_been_read = true) ->
    if (@get 'has_been_read') != has_been_read
      @set 'has_been_read', has_been_read
      @trigger 'set_as_read', @
      @updateOnRemoteServer()

  updateOnRemoteServer: () ->
    $.ajax {
      method: 'POST'
      url: @url()
      data: {
        post: {
          id:            @get 'id'
          has_been_read: @get 'has_been_read'
        }
      }
    }
