window.application = new class
  constructor: () ->
    $(document).ready => @initialize()

  initialize: () ->
    @host         = $('body').data 'context-path'
    @current_user = new CurrentUser()
    @current_user.on 'authenticate:connect',    -> Backbone.history.navigate 'home',  true
    @current_user.on 'authenticate:disconnect', -> Backbone.history.navigate 'login', true
    @current_user.check_connection()
    @initialize_collections()
    @initialize_controllers()
    Backbone.history.start()

  initialize_collections: () ->
    @feeds = new FeedCollection()

  initialize_controllers: () ->
    @home_controller    = new HomeController()
    @feeds_controller   = new FeedsController()

  disconnected: () ->
    Backbone.history.navigate 'login', true

  url_params: (url, params) ->
    if params?
      params_string = ''
      data = []
      data.push { name: key, value: value } for key,value of params
      params_string = '?' + $.param data
      "#{url}#{params_string}"
    else
      url

  url: (path, params) ->
    @url_params "#{@host}/#{path}", params

  notification: (message, type) ->
    if type?
      $.growl[type] { message: message }
    else
      $.growl       { message: message }
