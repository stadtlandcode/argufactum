basePath = '../';

files = [
  JASMINE,
  JASMINE_ADAPTER,
  'web/lib/angular/angular.js',
  'web/lib/angular/angular-*.js',
  'web/lib/underscore.js',
  'test/lib/angular/angular-mocks.js',
  'web/js/comparison/app.js',
  'web/js/comparison/controllers/*.js',
  'web/js/comparison/filters/*.js',
  'web/js/comparison/directives/*.js',
  'web/js/comparison/services/*.js',
  'test/unit/**/*.js'
];

autoWatch = true;

browsers = ['Chrome'];

junitReporter = {
  outputFile: 'test_out/unit.xml',
  suite: 'unit'
};
