window.application = new class
  constructor: () ->
    $(document).ready => @initialize()

  initialize: () ->
    @host         = $('body').data 'context-path'
    @current_user = new CurrentUser()
    @current_user.check_connection()
    @initialize_collections()
    @initialize_controllers()
    Backbone.history.start()

  initialize_collections: () ->
    @feeds = new FeedCollection()

  initialize_controllers: () ->
    @home_controller    = new HomeController()
    @feeds_controller   = new FeedsController()

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
