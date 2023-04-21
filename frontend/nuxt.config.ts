// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  modules: [
    '@nuxtjs/tailwindcss',
    'nuxt-headlessui',
    '@pinia/nuxt',
    '@nuxtjs/color-mode',
  ],
  colorMode: {
    classSuffix: ''
  },
  tailwindcss: {
    cssPath: '~/assets/global.css',
    configPath: 'tailwind.config.js',
    exposeConfig: false,
    config: {},
    injectPosition: 0,
    viewer: true,
  }
})
