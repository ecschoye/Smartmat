import { mount } from '@vue/test-utils';
import RecipeWeeklyCard from '~/components/WeeklyMenu/RecipeWeeklyCard.vue';
import { Recipe } from '~/types/RecipeType';
import LoginForm from "~/components/Form/LogInForm.vue";

const globalMocks = {
    $t: () => {}, // Add this line to mock the $t function
}
describe('YourComponentName.vue', () => {
    const mockRecipe: Recipe = {
        id: 1,
        name: 'Test Recipe',
        url: 'https://example.com/test-recipe.jpg',
        ingredients: [
            { name: 'Ingredient 1', quantity: '1 cup' },
            { name: 'Ingredient 2', quantity: '2 cups' },
        ],
    };

    const wrapper = mount(RecipeWeeklyCard, {
        global: {
            mocks: globalMocks,
            stubs: {
                NuxtLink: true, // Add this line to stub the nuxt-link component
            }
        },
        props: { recepeInfo: mockRecipe, lockedBoolean: false },
    });

    it('renders recipe name', () => {
        expect(wrapper.find('.recepe-title').text()).toBe(mockRecipe.name);
    });

    // Add more tests as needed
});
