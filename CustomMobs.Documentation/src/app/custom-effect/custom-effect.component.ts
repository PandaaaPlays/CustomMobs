import {ActivatedRoute, Router, RouterLink} from '@angular/router';
import {NgClass, NgForOf, NgIf} from '@angular/common';
import {Component, ElementRef, HostListener, Input, OnInit} from '@angular/core';
import {Option, OptionsService} from '../services/options.service';
import {FormsModule} from '@angular/forms';
import {CustomEffect, CustomEffectsService} from '../services/custom-effects.service';

@Component({
  selector: 'custommobs-custom-effect',
  standalone: true,
  imports: [NgForOf, NgIf, FormsModule],
  templateUrl: './custom-effect.component.html',
  styleUrl: './custom-effect.component.css'
})
export class CustomEffectComponent implements OnInit {
  className: string = '';
  customEffect: CustomEffect | null = null;
  title: string = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private customEffectsService: CustomEffectsService
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.className = params['name'];

      this.customEffectsService.customEffects$.subscribe(data => {
        const effect = data[this.className];
        if (effect) {
          this.customEffect = effect;
          switch (this.customEffect?.effectType) {
            case "COOLDOWN":
              this.title = "A random enabled 'Cooldown' custom effect(s) will be triggered every x second(s)."
              break;
            case "ON_IMPACT":
              this.title = "Enabled 'On impact' custom effect(s) will be triggered when a player is in a 1 block radius of the CustomMob."
              break;
            case "ON_DAMAGE_ON_PLAYER":
              this.title = "Enabled 'On damage on player' custom effect(s) will be triggered when a player is hurt by the CustomMob."
              break;
            default:
              this.title = "";
              break;
          }
        } else {
          this.router.navigate(['/']);
        }
      });
    });

  }

  formatClassName(name: string): string {
    return (name ?? '').replace(/([a-z])([A-Z])/g, '$1 $2');
  }
}
