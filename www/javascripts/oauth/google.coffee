window.oauth ||= {}

$(document).ready ->

  window.oauth.google = new class
    url:         'https://accounts.google.com/o/oauth2/auth'
    params:
      scope:         'email profile'
      client_id:     $('meta[name="google-client-id"]').attr('content')
      redirect_uri:  $('meta[name="google-redirect-url"]').attr('content')
      response_type: 'code'

    authenticate: () ->
      location.href = application.url_params @url, @params
