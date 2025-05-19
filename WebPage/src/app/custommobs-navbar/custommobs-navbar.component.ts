import {Component, OnInit} from '@angular/core';
import {RouterLink} from '@angular/router';
import {NavbarMenu} from './navbar-menu/navbar-menu.component';
import {OptionsService} from '../services/options.service';

interface Option {
  optionName: string,
  key: string,
  description: string
}

interface CustomEffect {
  effectName: string,
  option: string,
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
    private optionsService: OptionsService
  ) {}

  optionNames: string[] = [];
  customEffectNames: string[] = [];

  ngOnInit(): void {
    this.optionsService.options$.subscribe((data: { [key: string]: Option[] }) => {
      this.optionNames = Object.keys(data);
    });
  }
}
