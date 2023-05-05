import { mount } from '@vue/test-utils';
import SelectUnit from './SelectUnit.vue';
import { createPinia } from "pinia";
import { createRouter, createWebHistory } from "vue-router";
import register from "~/pages/register.vue";
import index from "~/pages/index.vue";
import ChangePassword from "~/components/Profile/ChangePassword.vue";


const pinia = createPinia()
const mockStore = {
    someGetter: 'mockValue'
}



const router = createRouter({
    history: createWebHistory(''),
    routes: [
        {
            path: '/register',
            component: register,
        },
        {
            path: '/',
            component: index,
        }
    ],
})

const localePath = (path: string) => router.resolve({ path }).href

const globalMocks = {
    $pinia: pinia,
    $store: mockStore,
    $t: () => {},
    localePath
}


describe('SelectUnit.vue', () => {
    let wrapper;

    const units = [
        { id: 1, name: 'Unit 1', weight: 100 },
        { id: 2, name: 'Unit 2', weight: 200 },
        { id: 3, name: 'Unit 3', weight: 300 },
    ];

    beforeEach(() => {
        wrapper = mount(SelectUnit, {
            props: {
                grocery: {
                    unit: units[0],
                    quantity: 1,
                },
            },
            global: {
                plugins: [pinia, router],
                mocks: globalMocks,
                stubs: {
                    NuxtLink: true,
                }
            },
        });
    });

    it('renders the component', () => {
        expect(wrapper.exists()).toBe(true);
    });
});
