// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  ssr: false,
  modules: [
    '@nuxtjs/tailwindcss',
    'nuxt-headlessui',
    '@pinia/nuxt',
    '@nuxtjs/color-mode',
    '@nuxtjs/i18n',
    '@pinia-plugin-persistedstate/nuxt',
  ],
  colorMode: {
    classSuffix: ''
  },
  tailwindcss: {
    cssPath: '~/assets/global.css',
    configPath: 'tailwind.config.js',
    exposeConfig: false,
    injectPosition: 0,
    viewer: true,
  },
  i18n: {
    lazy: true,
    langDir: 'locales',
    strategy: 'prefix_except_default',
    locales: [
      {
        code: 'no',
        iso: 'no',
        name: 'Norsk',
        file: 'no.json',
      },
      {
        code: 'en',
        iso: 'en',
        name: 'English (US)',
        file: 'en.json',
      },
      {
        code: 'de',
        iso: 'de',
        name: 'Deutsch',
        file: 'de.json',
      },
      {
        code: 'fr',
        iso: 'fr',
        name: 'Français',
        file: 'fr.json',
      },
      {
        code: 'es',
        iso: 'es',
        name: 'Español',
        file: 'es.json',
      },
      {
        code: 'zh',
        iso: 'zh',
        name: '中文 (Chinese) ',
        file: 'zh.json',
      },
      {
        code: 'hi',
        iso: 'hi',
        name: 'हिंदी (Hindi) ',
        file: 'hi.json',
      },
    ],
    customRoutes: 'config',
    pages: {
    },
    defaultLocale: 'no',
    vueI18n: '/i18n.config.ts'
  },

})
