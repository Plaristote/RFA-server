window.SessionViews ||= {}

class window.SessionViews.Create extends Backbone.View
  className: 'session-view'
  template:  JST['session_create']
  events:
    'submit form':                  'onFormSubmit'
    'click .google-authentication': 'onGoogleOauth'
    'click .login-authentication':  'onLoginAuth'

  render: () ->
    @$el.html @template()

  username: () ->
    @$el.find('input[name="username"]').val()

  password: () ->
    @$el.find('input[name="password"]').val()

  onFormSubmit: (e) ->
    e.preventDefault()
    application.current_user.connect {
      username: @username()
      password: @password()
      failure:  @onLoginFailed.bind @
    }

  onLoginFailed: () ->
    application.notification "Cannot authenticate as user '#{@username()}'", 'error'

  onGoogleOauth: () ->
    oauth.google.authenticate()

  onLoginAuth: () ->
    $('.oauth-modes', @$el).hide()
    $('.create-user', @$el).show()
