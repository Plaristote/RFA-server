window.SessionViews ||= {}

class window.SessionViews.Show extends Backbone.View
  className: 'session-view'
  template:  JST['session_show']
  events: {
    'click .disconnect-button': 'disconnect'
  }

  render: ->
    @$el.html @template { username: application.username }

  disconnect: ->
    application.current_user.disconnect {
      failure: @onDestroyFailure.bind @
    }

  onDestroyFailure: ->
    application.notification "User '#{@username}' could not be disconnected", 'error'
