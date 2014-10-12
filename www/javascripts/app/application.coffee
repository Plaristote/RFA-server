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

  url: (path, params) ->
    params_string = ''
    if params?
      data = []
      data.push { name: key, value: value } for key,value of params
      params_string = $.param data
    "#{@host}/#{path}?#{params_string}"

  notification: (message, type) ->
    if type?
      $.growl[type] { message: message }
    else
      $.growl       { message: message }
