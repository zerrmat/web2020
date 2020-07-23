import React from 'react';
import { render } from '@testing-library/react';
import AppDefault from './AppDefault';

test('renders learn react link', () => {
  const { getByText } = render(<AppDefault />);
  const linkElement = getByText(/learn react/i);
  expect(linkElement).toBeInTheDocument();
});
