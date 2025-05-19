import { Routes } from '@angular/router';
import {OptionComponent} from './option/option.component';

export const routes: Routes = [
  { path: '', component: OptionComponent },
  { path: 'option/:name', component: OptionComponent },
];
