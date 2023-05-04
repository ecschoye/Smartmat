import { expect, test } from 'vitest';
import { mount } from '@vue/test-utils';
import LoginForm from './LoginForm.vue';
import { createPinia } from 'pinia';
import { createRouter, createWebHistory } from 'vue-router'
import register from '../../pages/register.vue'
import index from '../../pages/index.vue'


const pinia = createPinia()
const mockStore = {
    someGetter: 'mockValue'
}

const globalMocks = {
    $pinia: pinia,
    $store: mockStore,
    $t: () => {}, // Add this line to mock the $t function
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
        },
    ],
})

const localePath = (path: string) => router.resolve({ path }).href

test('renders LoginForm component', () => {
    const wrapper = mount(LoginForm, {
        global: {
            plugins: [pinia, router],
            mocks: globalMocks,
            stubs: {
                NuxtLink: true, // Add this line to stub the nuxt-link component
            },
            provide: {
                localePath,
            },
        },
    });
    const input = wrapper.find('BaseInput');
    expect(input.exists()).toBe(true);
    let test = input.attributes();
});

test('renders email and password input fields', () => {
    const wrapper = mount(LoginForm, {
        global: {
            plugins: [pinia, router],
            mocks: globalMocks,
            stubs: {
                NuxtLink: true,
            },
            provide: {
                localePath,
            },
        },
    });
    const emailInput = wrapper.find('#inpEmail');
    const passwordInput = wrapper.find('#inpPassword');

    expect(emailInput.exists()).toBe(true);
    expect(passwordInput.exists()).toBe(true);
});

test('renders login and new user buttons with correct labels', () => {
    const wrapper = mount(LoginForm, {
        props: {
            form: {
                email: 'example@example.com',
                password: 'password',
            },
        },
        global: {
            plugins: [pinia],
            mocks: globalMocks,
            stubs: {
                NuxtLink: true, // Add this line to stub the nuxt-link component
            },
            provide: {
                localePath,
            },
        },
    });
    const loginButton = wrapper.find('#login');
    const newUserButton = wrapper.find('#new-user');

    expect(loginButton.exists()).toBe(true);
    expect(newUserButton.exists()).toBe(true);
    expect(loginButton.text()).toBe('log_in');
    expect(newUserButton.text()).toBe('new_user');
});
