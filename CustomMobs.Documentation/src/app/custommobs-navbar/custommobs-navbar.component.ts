import {Component, OnInit} from '@angular/core';
import {RouterLink} from '@angular/router';
import {NavbarMenu} from './navbar-menu/navbar-menu.component';
import {OptionsService} from '../services/options.service';
import {CustomEffectComponent} from '../custom-effect/custom-effect.component';
import {CustomEffectsService} from '../services/custom-effects.service';

interface Option {
  optionName: string,
  key: string,
  description: string
}

interface CustomEffect {
  customEffectName: string,
  key: string,
  description: string
}

@Component({
  selector: 'custommobs-navbar',
  templateUrl: './custommobs-navbar.component.html',
  styleUrls: ['./custommobs-navbar.component.css'],
  standalone: true,
  imports: [
    RouterLink,
    NavbarMenu
  ]
})

export class CustomMobsNavbar implements OnInit {
  constructor(
    private optionsService: OptionsService,
    private customEffectsService: CustomEffectsService
  ) {}

  optionNames: string[] = [];
  customEffectNames: string[] = [];

  ngOnInit(): void {
    this.optionsService.options$.subscribe(data => {
      this.optionNames = Object.keys(data);
    });

    this.customEffectsService.customEffects$.subscribe(data => {
      this.customEffectNames = Object.keys(data);
    });
  }
}
