function fn() {
    var base = java.lang.System.getProperty('BASE_URL') || 'http://127.0.0.1:9000';
    var config = {};
    config.baseUrl  = base;
    config.API_ROOT = base + '/api/auth';
    karate.log('karate-config.js BASE_URL =', base);
    return config;
}