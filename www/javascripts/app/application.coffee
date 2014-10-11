window.application = new class
  constructor: () ->
    $(document).ready => @initialize()
    @host = 'localhost:8080/aggregator'

  initialize: () ->
    @session_controller = new SessionController()
    @feeds_controller   = new FeedsController()
    Backbone.history.start()

  url: (path) ->
    "#{@host}/#{path}"

  notification: (message, type) ->
    if type?
      $.growl[type] { message: message }
    else
      $.growl       { message: message }
