class window.CurrentUser extends Backbone.Model
  url: -> application.url 'session'
  is_connected: ->
    @has 'email'

  check_connection: ->
    $.ajax {
      method: 'GET'
      url:     @url()
      success: (data) =>
        @set 'email', data.email
        @trigger 'authenticate'
        @trigger 'authenticate:connect'
      error: =>
        @trigger 'authenticate'
        @trigger 'authenticate:disconnect'
    }

  connect: (options = {}) ->
    $.ajax {
      method:  'POST'
      url:     "#{@url()}/new"
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
      url:     @url()
      success: =>
        @unset 'email'
        @trigger 'authenticate'
        @trigger 'authenticate:disconnect'
        options.success() if options.success?
      error: ->
        options.failure() if options.failure?
    }

  create: (email, password) ->
    $.ajax {
      method: 'POST'
      url:     @url()
      data:
        email:    email
        password: password
      success: =>
        @set 'email', email
        @trigger 'authenticate'
        @trigger  'authenticate:connect'
      error: ->
        application.notification "Email '#{email}' is already linked to an account"
    }
