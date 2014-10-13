class window.PostModel extends Backbone.Model
  url: null
  has_been_read: false

  hasBeenRead: () ->
    @has_been_read

  setAsRead: () ->
    @has_been_read = true
    @trigger 'set_as_read', @
