import { expect, test } from 'vitest';
import { mount } from '@vue/test-utils';
import BaseInput from './BaseInput.vue';

test('renders BaseInput component', () => {
    const wrapper = mount(BaseInput, {
        props: {
            id: 'test-id',
            label: 'Test Label',
            modelValue: 'Test Value',
            type: 'text',
            cutWidth: '70px',
        },
    });

    const input = wrapper.find('input');
    expect(input.exists()).toBe(true);
    expect(input.attributes('id')).toBe('test-id');
    expect(input.attributes('type')).toBe('text');
    expect(input.element.value).toBe('Test Value');

    const label = wrapper.find('label');
    expect(label.exists()).toBe(true);
    expect(label.text()).toBe('Test Label');
});
