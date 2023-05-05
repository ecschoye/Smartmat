import { mount } from '@vue/test-utils'
import FavoriteToggler from '~/components/Button/FavoriteToggler.vue'

describe('FavoriteToggler', () => {
    it('renders correctly with default props', () => {
        const wrapper = mount(FavoriteToggler)
        expect(wrapper.element).toMatchSnapshot()
    })

    it('renders correctly with isFavorite=true', () => {
        const wrapper = mount(FavoriteToggler, {
            props: {
                isFavorite: true,
            },
        })
        expect(wrapper.element).toMatchSnapshot()
    })

    it('renders correctly with large=true', () => {
        const wrapper = mount(FavoriteToggler, {
            props: {
                large: true,
            },
        })
        expect(wrapper.element).toMatchSnapshot()
    })

    it('renders correctly with text=false', () => {
        const wrapper = mount(FavoriteToggler, {
            props: {
                text: false,
            },
        })
        expect(wrapper.element).toMatchSnapshot()
    })

    it('emits "favoriteEvent" with true when clicked and isFavorite=false', async () => {
        const wrapper = mount(FavoriteToggler, {
            props: {
                isFavorite: false,
            },
        })
        await wrapper.trigger('click')
        expect(wrapper.emitted('favoriteEvent')).toEqual([[true]])
    })

    it('emits "favoriteEvent" with false when clicked and isFavorite=true', async () => {
        const wrapper = mount(FavoriteToggler, {
            props: {
                isFavorite: true,
            },
        })
        await wrapper.trigger('click')
        expect(wrapper.emitted('favoriteEvent')).toEqual([[false]])
    })
})
