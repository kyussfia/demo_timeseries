import React from 'react';
import { AddGreeting } from './AddGreeting';
import { GreetingView } from './GreetingView';
import { DeleteGreeting } from './DeleteGreeting';
import FileUploadComponent from "./FileUploadComponent";
import ParentComponent from "./ParentComponent";

export const App = () => {
  return (
    <div>
      <GreetingView />
      <hr/>
      <AddGreeting />
      <hr/>
      <DeleteGreeting />
      <hr/>
      <FileUploadComponent />
      <hr/>
      <ParentComponent />
    </div>
  );
};
