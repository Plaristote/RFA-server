class window.HomeController extends Backbone.Router
  routes: {
    "home": "show"
  }

  constructor: () ->
    super
    @view = new HomeView()
    @view.render()

  show: () ->
    ;
