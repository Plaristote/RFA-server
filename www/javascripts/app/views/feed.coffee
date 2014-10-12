class window.FeedView extends Backbone.View
  template: JST['feed']

  render: () ->
    @$el.html @template { feed: @feed, posts: @posts }
    $('#main-content').empty().append @$el
