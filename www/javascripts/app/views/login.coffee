class window.LoginView extends Backbone.View
  className: 'login-view'
  template:  JST['login']

  events:
    "click button.subscribe": "subscribe"

  render: () ->
    @$el.html @template()
    $('#main-content').empty().append @$el

  subscribe: (e) ->
    e.preventDefault()
    username     = $('[name="user[email]"]',                 @$el).val()
    password     = $('[name="user[password]"]',              @$el).val()
    confirmation = $('[name="user[password_confirmation]"]', @$el).val()
    if password != confirmation
      application.notification 'password and confirmation don\'t match'
    else
      application.current_user.create username, password

