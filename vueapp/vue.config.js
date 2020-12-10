const path = require("path");
const vueSrc = "./src";

module.exports = {
    //TODO: check if theres a way to do this better without a need for an actual path
    publicPath: process.env.NODE_ENV === 'production' ? '/paw-2020a-9/' : '/',

    pluginOptions: {
      i18n: {
        locale: 'en',
        fallbackLocale: 'en',
        localeDir: 'locales',
        enableInSFC: false
      }
    },
    devServer: {
      /* 
      add a .env.local with this property
      example:
      LOCAL_PROXY_SERVER = 'http://localhost:8080/medicare'
      */
      proxy: {
          "^/api/": {
              target: process.env.LOCAL_PROXY_SERVER,
              logLevel: "debug"
          }
      }
    },
    configureWebpack: {
        resolve: {
            alias: {
                '@': path.join(__dirname, vueSrc),
                '~': path.join(__dirname, vueSrc),
            }
        }
    }
};
