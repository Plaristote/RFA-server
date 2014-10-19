class window.HomeController extends Backbone.Router
  routes: {
    "home":  "show"
    "login": "login"
  }

  constructor: () ->
    super
    @view = new HomeView()
    @view.render()

  show: () ->
    view = new HomepageView()
    view.render()

  login: () ->
    view = new LoginView()
    view.render()
