var gulp            = require('gulp');
var coffee          = require('gulp-coffee');
var eco             = require('gulp-eco');
var concat          = require('gulp-concat');
var uglify          = require('gulp-uglify');
var sass            = require('gulp-ruby-sass');
var gulpif          = require('gulp-if');
var require_nocache = require('require-without-cache');
var assets          = require_nocache('./config/assets.json', require);
var destination     = "../WebContent/assets";

var debug  = true;

var paths = {
  scripts: [ 'javascripts/**/*.js',   'javascripts/**/*.coffee' ],
  css:     [ 'stylesheets/**/*.scss', 'stylesheets/**/*.css' ],
  eco:     [ 'templates/**/*.eco' ]
}

gulp.task('scripts', ['clean'], function() {
  for (output_file in assets.javascripts)
  {
    var scripts_paths = [];
    var scripts;

    for (var i = 0 ; i < assets.javascripts[output_file].length ; ++i)
      scripts_paths.push('javascripts/' + assets.javascripts[output_file][i]);
    scripts   = gulp.src(scripts_paths).pipe(gulpif(/[.]coffee$/, coffee()));
    if (debug == false)
      scripts = scripts.pipe(uglify);
    scripts.pipe(concat(output_file + '.js')).pipe(gulp.dest(destination));
  }
});

gulp.task('css', ['clean'], function() {
  for (output_file in assets.stylesheets)
  {
    var scripts_paths = [];
    var scripts;

    for (var i = 0 ; i < assets.stylesheets[output_file].length ; ++i)
      scripts_paths.push('stylesheets/' + assets.stylesheets[output_file][i]);
    scripts   = gulp.src(scripts_paths).pipe(gulpif(/[.]scss$/, sass({ sourcemap: false })));
    scripts.pipe(concat(output_file + '.css')).pipe(gulp.dest(destination));
  }
});

gulp.task('eco', ['clean'], function() {
  gulp.src(paths.eco).pipe(eco()).pipe(gulp.dest('javascripts/templates'));
});

gulp.task('assets.json', ['clean'], function() {
  assets = require_nocache('./config/assets.json', require);
});

gulp.task('clean', function() {
});

gulp.task('watch', function() {
  gulp.watch(['config/assets.json'], ['assets.json', 'eco', 'scripts', 'css']);
  gulp.watch(paths.eco,     ['eco']);
  gulp.watch(paths.scripts, ['scripts']);
  gulp.watch(paths.css,     ['css']);
});

gulp.task('default', ['watch', 'eco', 'scripts', 'css']);
