import {ActivatedRoute, Router, RouterLink} from '@angular/router';
import {NgClass, NgForOf, NgIf} from '@angular/common';
import {Component, ElementRef, HostListener, Input, OnInit} from '@angular/core';
import {Option, OptionsService} from '../services/options.service';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'custommobs-option',
  standalone: true,
  imports: [NgForOf, NgIf, FormsModule, NgClass, RouterLink],
  templateUrl: './landing.component.html',
  styleUrl: './landing.component.css'
})
export class LandingComponent implements OnInit {
  className: string = '';
  options: Option[] = [];
  searchTerm: string = '';
  filteredOptions: any[] = [];
  searchResults: { entityType: string; options: string[] }[] = [];
  searching: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private optionsService: OptionsService
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.className = params['name'];

      this.optionsService.options$.subscribe((data: { [key: string]: Option[] }) => {
        if (data[this.className]) {
          this.options = data[this.className];
        } else {
          this.router.navigate(['/']);
        }
      });
    });
    this.filteredOptions = this.options;
  }

  onSearchFocus() {
    if (this.searchTerm?.trim()) {
      this.onSearchChange();
    }
  }

  @HostListener('document:click', ['$event'])
  onClick(event: MouseEvent) {
    const targetElement = event.target as HTMLElement;

    const clickedInsideInput = targetElement.closest('.searchbar') || targetElement.closest('.search-results');

    if (!clickedInsideInput) {
      this.searchResults = [];
    }

    this.searching = clickedInsideInput != null;
  }

  onSearchChange() {
    const term = this.searchTerm?.toLowerCase().trim() ?? '';
    if (term === '') {
      this.searchResults = [];
      return;
    }

    this.optionsService.entityTypes$.subscribe((entityMap: { [entityType: string]: string[] }) => {
      const entries = Object.entries(entityMap).map(([entityType, options]) => {
        const formattedType = entityType.toLowerCase().replaceAll('_', ' ');
        return { entityType, formattedType, options };
      });

      let filtered = [];

      filtered = entries.filter(entry => entry.formattedType.includes(term));
      if (filtered.length > 7) {
        filtered = entries.filter(entry => entry.formattedType.startsWith(term));
      }

      this.searchResults = filtered.map(({ entityType, options }) => ({
        entityType,
        options,
      }));
    });
  }

  resetSearch(): void {
    this.searchResults = [];
    this.searchTerm = '';
  }

  formatClassName(name: string): string {
    return (name ?? '').replace(/([a-z])([A-Z])/g, '$1 $2');
  }

  formatOptions(options: string[]): string {
    return `Options: ${options.join(', ')}`;
  }
}
