class window.HomepageView extends Backbone.View
  className: 'home-page'
  template:  JST['homepage']

  render: () ->
    @$el.html @template()
    $('#main-content').empty().append @$el
