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
    if(this.menuItems.length === 0) {
      this.router.navigate(['/', path]);
    } else {
      this.router.navigate(['/' + this.menuName.replaceAll(" ", "-").toLowerCase(), path]);
    }
  }

  formatMenuItem(name: string): string {
    return name.replace(/([a-z])([A-Z])/g, '$1 $2');
  }

  isItemSelected(name: string): boolean {
    if(name == "About" && window.location.pathname.split('/')[2] == "") {
      return true;
    }
    if(name == "Tutorial" && window.location.pathname.split('/')[2] == "Tutorial") {
      return true;
    }
    if(name == "Examples" && window.location.pathname.split('/')[2] == "Examples") {
      return true;
    }

    return window.location.pathname.split('/')[3] != ""
      && window.location.pathname.split('/')[3] == name;
  }
}
