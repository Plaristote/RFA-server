window.application = new class
  constructor: () ->
    $(document).ready => @initialize()
    @host = '/someproject'

  initialize: () ->
    @current_user = new CurrentUser()
    @initialize_collections()
    @initialize_controllers()
    Backbone.history.start()

  initialize_collections: () ->
    @feeds = new FeedCollection()

  initialize_controllers: () ->
    @home_controller    = new HomeController()
    @feeds_controller   = new FeedsController()

  url: (path) ->
    "#{@host}/#{path}"

  notification: (message, type) ->
    if type?
      $.growl[type] { message: message }
    else
      $.growl       { message: message }
