import { Routes } from '@angular/router';
import {OptionComponent} from './option/option.component';
import {LandingComponent} from './landing/landing.component';

export const routes: Routes = [
  { path: '', component: LandingComponent },
  { path: 'option/:name', component: OptionComponent },
];
