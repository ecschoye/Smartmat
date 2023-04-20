// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  modules: [
    '@nuxtjs/tailwindcss',
    'nuxt-headlessui',
    '@pinia/nuxt',
  ],
  components: [
    { path: '~/components/refridgerator', prefix: 'refridgerator' },
    '~/components'
  ]
})
