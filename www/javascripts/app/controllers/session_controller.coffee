class window.SessionController extends Backbone.Router
  routes: {
    "session/create":  "create",
    "session/destroy": "destroy"
  }

  create: ->
    view = new SessionViews.Create()
    view.render()
    view.onCreate = @onCreate.bind @
    $('body').append view.$el

  destroy: ->
    $.ajax {
      method:  'DELETE'
      url:     application.url 'session'
      success: @onDestroySuccess.bind @
      error:   @onDestroyFailure.bind @
    }

  onCreate: (username, password) ->
    alert "Creating session for #{username} with password #{password}"
    @username = username
    $.ajax {
      method:  'POST'
      url:     application.url 'session'
      success: @onCreateSuccess.bind @
      error:   @onCreateFailure.bind @
      data: { email: username, password: password }
    }

  onCreateSuccess: ->
    alert 'create success'

  onCreateFailure: ->
    application.notification "User '#{@username}' could not be authentified", 'error'
    Backbone.history.navigate '/sessions/create', true

  onDestroySuccess: ->
    alert 'destroy success'

  onDestroyFailure: ->
    application.notification "User '#{@username}' could not be disconnected", 'error'
