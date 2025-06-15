import {Component, Input, OnInit} from '@angular/core';
import {NgClass, NgForOf, NgIf} from '@angular/common';
import {NavigationEnd, Router} from '@angular/router';
import {filter} from 'rxjs';

@Component({
  selector: 'navbar-menu',
  imports: [
    NgIf,
    NgClass,
    NgForOf
  ],
  templateUrl: './navbar-menu.component.html',
  styleUrl: './navbar-menu.component.css',
  standalone: true
})
export class NavbarMenu {
  selected: boolean = false;
  @Input() menuName: string = 'MenuName';
  @Input() iconClass: string = 'fa-solid fa-x';
  @Input() menuItems: string[] = [];

  constructor(private router: Router) {}

  toggleSelected() {
    this.selected = !this.selected;
  }

  navigateTo(path: string) {
    console.log('/' + this.menuName.replaceAll(" ", "-"));
    this.router.navigate(['/' + this.menuName.replaceAll(" ", "-").toLowerCase(), path]);
  }

  formatMenuItem(name: string): string {
    return name.replace(/([a-z])([A-Z])/g, '$1 $2');
  }

  isItemSelected(name: string): boolean {
    return window.location.pathname.split('/')[3] != ""
      && window.location.pathname.split('/')[3] == name;
  }
}
