class window.CurrentUser extends Backbone.Model
  is_connected: ->
    @has 'email'

  connect: (options = {}) ->
    $.ajax {
      method:  'POST'
      url:     application.url 'session'
      success: =>
        @set 'email', options.username
        @trigger 'authenticate'
        @trigger 'authenticate:connect'
        options.success() if options.success?
      error:  ->
        options.failure() if options.failure?
      data: { email: options.username, password: options.password }
    }

  disconnect: (options = {}) ->
    $.ajax {
      method:  'DELETE'
      url:     application.url 'session'
      success: =>
        @unset 'email'
        @trigger 'authenticate'
        @trigger 'authenticate:disconnect'
        options.success() if options.success?
      error: ->
        options.failure() if options.failure?
    }
