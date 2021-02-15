const path = require("path");
const vueSrc = "./src";

module.exports = {
  // TODO:check
  publicPath: process.env.VUE_APP_ROOT_PATH ? process.env.VUE_APP_ROOT_PATH : "/",
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
