import {Component, OnInit} from '@angular/core';
import {CustomMobsNavbar} from './custommobs-navbar/custommobs-navbar.component';
import {NavigationEnd, Router, RouterOutlet} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {OptionsService} from './services/options.service';
import {Title} from '@angular/platform-browser';
import {filter} from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
  imports: [CustomMobsNavbar, RouterOutlet],
  standalone: true
})
export class AppComponent implements OnInit {
  constructor(
    private http: HttpClient,
    private optionsService: OptionsService,
    private titleService: Title,
    private router: Router
  ) {}
  options: { optionName: string; key: string; description: string }[] = [];

  ngOnInit() {
    this.optionsService.fetchOptions();

    this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe((event: NavigationEnd) => {
        let path = event.urlAfterRedirects;
        let title = 'CustomMobs'; // default title

        // Set title based on path
        if (path.includes('/option')) {
          title = 'CustomMobs - ' + path.split('/')[2] + ' (Option)';
        } else if (path.includes('/customeffect')) {
          title = 'CustomMobs - ' + path.split('/')[2] + ' (Custom effect)';
        }

        this.titleService.setTitle(title);
      });
  }
}
