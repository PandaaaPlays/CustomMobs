import { Routes } from '@angular/router';
import {OptionComponent} from './option/option.component';
import {LandingComponent} from './landing/landing.component';
import {CustomEffectComponent} from './custom-effect/custom-effect.component';
import {ExamplesComponent} from './examples/examples.component';
import {TutorialComponent} from './tutorial/tutorial.component';

export const routes: Routes = [
  { path: '', component: LandingComponent },
  { path: 'Examples', component: ExamplesComponent },
  { path: 'Tutorial', component: TutorialComponent },
  { path: 'Options/:name', component: OptionComponent },
  { path: 'Custom-effects/:name', component: CustomEffectComponent },
];
