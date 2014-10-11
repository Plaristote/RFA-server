window.SessionViews ||= {}

class window.SessionViews.Create extends Backbone.View
  template: JST['session_create']
  events: {
    'submit form': 'onFormSubmit'
  }

  render: () ->
    @$el.html @template()

  onFormSubmit: (e) ->
    e.preventDefault()
    if @onCreate?
      username = @$el.find('input[name="username"]').val()
      password = @$el.find('input[name="password"]').val()
      @onCreate username, password
