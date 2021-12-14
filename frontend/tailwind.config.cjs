const production = !process.env.ROLLUP_WATCH;
module.exports = {
  future: {
    purgeLayersByDefault: true,
    removeDeprecatedGapUtilities: true,
  },
  plugins: [
  ],
  purge: {
    content: ['./public/index.html', './src/**/*.svelte'],
    enabled: production // disable purge in dev
  },
  mode: 'jit',
};