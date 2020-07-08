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
    }
}
