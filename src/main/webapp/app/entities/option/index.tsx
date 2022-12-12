import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Option from './option';
import OptionDetail from './option-detail';
import OptionUpdate from './option-update';
import OptionDeleteDialog from './option-delete-dialog';

const OptionRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Option />} />
    <Route path="new" element={<OptionUpdate />} />
    <Route path=":id">
      <Route index element={<OptionDetail />} />
      <Route path="edit" element={<OptionUpdate />} />
      <Route path="delete" element={<OptionDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default OptionRoutes;
