import { Routes } from '@angular/router';
import {OptionComponent} from './option/option.component';
import {LandingComponent} from './landing/landing.component';
import {CustomEffectComponent} from './custom-effect/custom-effect.component';

export const routes: Routes = [
  { path: '', component: LandingComponent },
  { path: 'options/:name', component: OptionComponent },
  { path: 'custom-effects/:name', component: CustomEffectComponent },
];
